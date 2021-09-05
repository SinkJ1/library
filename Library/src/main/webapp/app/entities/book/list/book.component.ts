import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBook } from '../book.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { BookService } from '../service/book.service';
import { BookDeleteDialogComponent } from '../delete/book-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

import { BookPermissionDialogComponent } from '../permission/book-permission-dialog.component';
import { BookPermissionDeleteDialogComponent } from '../permission/delete-permission/book-permission-delete-dialog.component';

@Component({
  selector: 'jhi-book',
  templateUrl: './book.component.html',
})
export class BookComponent implements OnInit {
  books: IBook[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  masStatus: any[] = [];
  canDo = false;

  constructor(protected bookService: BookService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.books = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }
  async postData(url = ''): Promise<string> {
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

  apiStatusRecordByFetch(): void {
    this.postData('https://practice.sqilsoft.by/internship/yury_sinkevich/acl/api/get-acl-entries?objE=sinkj1.library.domain.Book').then(
      data => {
        this.masStatus = JSON.parse(data);
      }
    );
  }

  apiCanDoByFetch(): void {
    this.postData('https://practice.sqilsoft.by/internship/yury_sinkevich/acl/api/check-role').then(data => {
      this.canDo = JSON.parse(data);
    });
  }

  getStatusRecord(idRecord: any, permissionId: number): any {
    if (this.canDo) {
      return 16;
    }
    const newElemet: any = this.masStatus.filter(masStat => masStat.objId === idRecord);
    if (newElemet.length === 0) {
      return 0;
    }

    for (let i = 0; i < newElemet.length; i++) {
      if (newElemet[i].mask === permissionId) {
        return true;
      }
    }

    return false;
  }

  permission(): void {
    const modalRef = this.modalService.open(BookPermissionDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.books = this.books;
    modalRef.closed.subscribe(reason => {
      if (reason === 'add') {
        console.log(modalRef.componentInstance.books);
      }
    });
  }

  deletePermission(): void {
    const modalRef = this.modalService.open(BookPermissionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });

    modalRef.closed.subscribe(reason => {
      if (reason === 'add') {
        console.log(modalRef.componentInstance.books);
      }
    });
  }

  loadAll(): void {
    this.apiStatusRecordByFetch();
    this.apiCanDoByFetch();
    this.isLoading = true;

    this.bookService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IBook[]>) => {
          this.isLoading = false;
          this.paginateBooks(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.books = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBook): number {
    return item.id!;
  }

  delete(book: IBook): void {
    const modalRef = this.modalService.open(BookDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.book = book;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateBooks(data: IBook[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.books.push(d);
      }
    }
  }
}
