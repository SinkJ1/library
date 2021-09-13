import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IBook } from '../../book.model';
import { BookService } from '../../service/book.service';

class BookPermission {
  id?: number;
  name?: string;
  mask?: string;
}

class DeletedPermissionBook {
  entityId?: number;
  permission?: string;
  userCredentional?: string;
}

@Component({
  templateUrl: './book-permission-delete-dialog.component.html',
  selector: 'jhi-book',
})
export class BookPermissionDeleteDialogComponent {
  books?: BookPermission[];
  permission: string;

  permissionForm = this.fb.group({
    entityId: [],
    userCredentional: [],
  });
  constructor(protected bookService: BookService, protected activeModal: NgbActiveModal, protected fb: FormBuilder) {
    this.permission = '';
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  downloadBooks(): void {
    const user: string = this.permissionForm.get(['userCredentional'])!.value;
    this.getData(`https://practice.sqilsoft.by/internship/yury_sinkevich/library/api/books/by-user/${user}`).then(data => {
      this.books = JSON.parse(data);
    });
  }

  async postData(url = '', sendData: any): Promise<string> {
    const check = sessionStorage.getItem('jhi-authenticationToken');
    let token = `Bearer ${check ? check : ''}`;
    token = token.replace('"', '');
    token = token.replace('"', '');
    const response: any = await fetch(url, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        Authorization: token,
        'X-TENANT-ID': 'yuradb',
      },
      body: JSON.stringify(sendData),
    });

    const data: string = await response.text();

    return data;
  }

  async getData(url = ''): Promise<string> {
    const check = sessionStorage.getItem('jhi-authenticationToken');
    let token = `Bearer ${check ? check : ''}`;
    token = token.replace('"', '');
    token = token.replace('"', '');
    const response: any = await fetch(url, {
      method: 'GET',

      headers: {
        Authorization: token,
        'X-TENANT-ID': 'yuradb',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'POST, GET, OPTIONS',
        'Access-Control-Allow-Headers': 'X-PINGOTHER, Content-Type',
        'Access-Control-Max-Age': '0',
        'Content-Security-Policy': 'default-src *; connect-src *; script-src *; object-src *',
        'X-Content-Security-Policy': 'default-src *; connect-src *; script-src *; object-src *',
        'X-Webkit-CSP': 'default-src *; connect-src *; script-src unsafe-inline unsafe-eval *; object-src *',
      },
    });

    const data: string = JSON.stringify(await response.json());

    return data;
  }

  deletePermission(bookId: number, mask: string): void {
    const book: BookPermission[] = this.books!.filter(value => value.id === bookId);
    const user: string = this.permissionForm.get(['userCredentional'])!.value;
    const toDelete: DeletedPermissionBook = {
      entityId: bookId,
      permission: book[0].mask,
      userCredentional: user,
    };
    console.log(this.postData('https://practice.sqilsoft.by/internship/yury_sinkevich/library/api/books/delete-permission/user', toDelete));
    this.books = this.books!.filter(value => {
      if (value.id === bookId && value.mask === mask) {
        return false;
      }
      return true;
    });
  }
}
