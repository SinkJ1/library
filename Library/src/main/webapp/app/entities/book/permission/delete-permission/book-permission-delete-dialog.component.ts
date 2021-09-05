import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IBook } from '../../book.model';
import { BookService } from '../../service/book.service';

class BookPermission {
  entityId?: number;
  permission?: string;
  userCredentional?: string;
}

@Component({
  templateUrl: './book-permission-delete-dialog.component.html',
  selector: 'jhi-book',
})
export class BookPermissionDeleteDialogComponent {
  books?: IBook[];
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

    const data: string = JSON.stringify(await response.json());

    return data;
  }

  selectPermission(e: any): void {
    this.permission = e.target.value;
  }

  deletePermission(): void {
    if (!this.permissionForm.get(['entityId'])?.value || !this.permissionForm.get(['userCredentional'])?.value) {
      alert('fill all fields');
    } else {
      const deletedPermission: BookPermission = {
        entityId: this.permissionForm.get(['entityId'])?.value,
        permission: this.permission ? this.permission : 'WRITE',
        userCredentional: this.permissionForm.get(['userCredentional'])?.value,
      };

      console.log(this.postData('http://localhost:8080/api/books/delete-test', deletedPermission));
    }
  }
}
