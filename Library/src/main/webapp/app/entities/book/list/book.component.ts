import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBook } from '../book.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { BookService } from '../service/book.service';
import { BookDeleteDialogComponent } from '../delete/book-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

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

apiStatusRecord(): void{
     const set = (masStat:any):void => {
       this.masStatus = masStat
     }
     const x = new XMLHttpRequest();
     x.open("GET", "http://localhost:8085/api/get-acl-entries?objE=sinkj1.library.domain.Book", false);

    const check = sessionStorage.getItem("jhi-authenticationToken")
    let token = `Bearer ${check ? check : ''}`
    token = token.replace('"', '')
    token = token.replace('"', '')
      x.setRequestHeader('Authorization', token);
      x.send();
      set(JSON.parse(x.responseText))
    }
    
    getStatusRecord  (idRecord:any):any {
      console.log(this.masStatus)
     const newElemet :any = this.masStatus.filter((masStat) => masStat.objId === idRecord );
     if (newElemet.length===0){return 0;}
     return newElemet[0].mask;
  }

  loadAll(): void {
    this.apiStatusRecord();
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
