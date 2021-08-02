import { IBook } from 'app/entities/book/book.model';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IPurchase {
  id?: number;
  cost?: number | null;
  book?: IBook | null;
  customer?: ICustomer | null;
}

export class Purchase implements IPurchase {
  constructor(public id?: number, public cost?: number | null, public book?: IBook | null, public customer?: ICustomer | null) {}
}

export function getPurchaseIdentifier(purchase: IPurchase): number | undefined {
  return purchase.id;
}
