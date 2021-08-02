jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PurchaseService } from '../service/purchase.service';
import { IPurchase, Purchase } from '../purchase.model';
import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

import { PurchaseUpdateComponent } from './purchase-update.component';

describe('Component Tests', () => {
  describe('Purchase Management Update Component', () => {
    let comp: PurchaseUpdateComponent;
    let fixture: ComponentFixture<PurchaseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let purchaseService: PurchaseService;
    let bookService: BookService;
    let customerService: CustomerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PurchaseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PurchaseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PurchaseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      purchaseService = TestBed.inject(PurchaseService);
      bookService = TestBed.inject(BookService);
      customerService = TestBed.inject(CustomerService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Book query and add missing value', () => {
        const purchase: IPurchase = { id: 456 };
        const book: IBook = { id: 19924 };
        purchase.book = book;

        const bookCollection: IBook[] = [{ id: 43472 }];
        jest.spyOn(bookService, 'query').mockReturnValue(of(new HttpResponse({ body: bookCollection })));
        const additionalBooks = [book];
        const expectedCollection: IBook[] = [...additionalBooks, ...bookCollection];
        jest.spyOn(bookService, 'addBookToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ purchase });
        comp.ngOnInit();

        expect(bookService.query).toHaveBeenCalled();
        expect(bookService.addBookToCollectionIfMissing).toHaveBeenCalledWith(bookCollection, ...additionalBooks);
        expect(comp.booksSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Customer query and add missing value', () => {
        const purchase: IPurchase = { id: 456 };
        const customer: ICustomer = { id: 96995 };
        purchase.customer = customer;

        const customerCollection: ICustomer[] = [{ id: 93425 }];
        jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
        const additionalCustomers = [customer];
        const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
        jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ purchase });
        comp.ngOnInit();

        expect(customerService.query).toHaveBeenCalled();
        expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(customerCollection, ...additionalCustomers);
        expect(comp.customersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const purchase: IPurchase = { id: 456 };
        const book: IBook = { id: 19553 };
        purchase.book = book;
        const customer: ICustomer = { id: 61501 };
        purchase.customer = customer;

        activatedRoute.data = of({ purchase });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(purchase));
        expect(comp.booksSharedCollection).toContain(book);
        expect(comp.customersSharedCollection).toContain(customer);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Purchase>>();
        const purchase = { id: 123 };
        jest.spyOn(purchaseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ purchase });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: purchase }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(purchaseService.update).toHaveBeenCalledWith(purchase);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Purchase>>();
        const purchase = new Purchase();
        jest.spyOn(purchaseService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ purchase });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: purchase }));
        saveSubject.complete();

        // THEN
        expect(purchaseService.create).toHaveBeenCalledWith(purchase);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Purchase>>();
        const purchase = { id: 123 };
        jest.spyOn(purchaseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ purchase });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(purchaseService.update).toHaveBeenCalledWith(purchase);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBookById', () => {
        it('Should return tracked Book primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBookById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCustomerById', () => {
        it('Should return tracked Customer primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCustomerById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
