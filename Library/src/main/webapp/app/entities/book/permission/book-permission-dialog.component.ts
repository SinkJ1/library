import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IBook } from '../book.model';
import { BookService } from '../service/book.service';

class BookPermission {
  entityId?: number;
  permission?: string;
  userCredentional?: string;
}

@Component({
  templateUrl: './book-permission-dialog.component.html',
  selector: 'jhi-book',
})
export class BookPermissionDialogComponent {
  books?: IBook[];
  selectedBooks: number[];
  bufferPermissionArray: BookPermission[];

  permissionForm = this.fb.group({
    userCredentional: '',
    permissions: [],
    bookName: '',
  });

  constructor(protected bookService: BookService, protected activeModal: NgbActiveModal, protected fb: FormBuilder) {
    (this.bufferPermissionArray = []), (this.selectedBooks = []);
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  check(bookId: number, isChecked: any): void {
    const user = this.permissionForm.get(['userCredentional'])!.value;
    if (bookId === 0 && isChecked.checked) {
      const bookPermission: BookPermission = {
        entityId: bookId,
        permission: 'CREATE',
        userCredentional: user,
      };
      this.bufferPermissionArray.push(bookPermission);
    } else if (bookId === 0) {
      this.bufferPermissionArray = this.bufferPermissionArray.filter(value => value.entityId !== bookId);
    } else if (isChecked.checked) {
      this.selectedBooks.push(bookId);
    } else {
      this.selectedBooks = this.selectedBooks.filter(value => value !== bookId);
      this.bufferPermissionArray = this.bufferPermissionArray.filter(value => value.entityId !== bookId);
    }
  }

  addAction(bookId: number): void {
    const selectedPermissions: string[] = this.permissionForm.get(['permissions'])!.value;
    const user = this.permissionForm.get(['userCredentional'])!.value;
    const newPermissions: BookPermission[] = [];
    selectedPermissions.map(value => {
      const bookPermission: BookPermission = {
        entityId: bookId,
        permission: value,
        userCredentional: user,
      };
      newPermissions.push(bookPermission);
    });
    const bufferArray = this.bufferPermissionArray.filter(value => value.entityId !== bookId);
    const concatedArray = bufferArray.concat(newPermissions);
    this.bufferPermissionArray = concatedArray;
  }

  getBooks(): IBook[] {
    const bookName: string = this.permissionForm.get(['bookName'])!.value;
    return this.books!.filter(value => value.name!.toLowerCase().indexOf(bookName.toLowerCase()) !== -1);
  }

  showPermissions(bookId: number): any {
    const permission: number[] = this.selectedBooks.filter(value => value === bookId);
    if (permission.length > 0) {
      return true;
    }
  }

  showAddPermissionsButton(): any {
    if (this.bufferPermissionArray.length === 0) {
      return true;
    }
  }

  checkUserCredentional(): any {
    return this.permissionForm.get(['userCredentional'])!.value;
  }

  async postData(url = '', sendData: any): Promise<string> {
    const check = sessionStorage.getItem('jhi-authenticationToken');
    let token = `Bearer ${check ? check : ''}`;
    token = token.replace('"', '');
    token = token.replace('"', '');
    const response: any = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: token,
      },
      body: JSON.stringify(sendData),
    });

    const data: string = JSON.stringify(await response.text());

    return data;
  }

  confirmAdd(): void {
    const user = this.permissionForm.get(['userCredentional'])!.value;
    if (!user) {
      this.bufferPermissionArray.length = 0;
      this.selectedBooks.length = 0;
      alert('write user name or role');
    } else {
      console.log(
        this.postData(
          'https://practice.sqilsoft.by/internship/yury_sinkevich/library/api/books/permissions/user',
          this.bufferPermissionArray
        )
      );
      this.activeModal.close('added');
    }
  }
}
