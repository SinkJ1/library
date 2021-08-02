import * as dayjs from 'dayjs';
import { IPurchase } from 'app/entities/purchase/purchase.model';

export interface ICustomer {
  id?: number;
  name?: string;
  birthday?: dayjs.Dayjs;
  purchases?: IPurchase[] | null;
}

export class Customer implements ICustomer {
  constructor(public id?: number, public name?: string, public birthday?: dayjs.Dayjs, public purchases?: IPurchase[] | null) {}
}

export function getCustomerIdentifier(customer: ICustomer): number | undefined {
  return customer.id;
}
