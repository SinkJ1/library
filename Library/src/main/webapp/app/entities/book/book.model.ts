import * as dayjs from 'dayjs';
import { IPurchase } from 'app/entities/purchase/purchase.model';
import { IAuthor } from 'app/entities/author/author.model';

export interface IBook {
  id?: number;
  name?: string;
  dateOfIssue?: dayjs.Dayjs;
  purchases?: IPurchase[] | null;
  authors?: IAuthor[] | null;
}

export class Book implements IBook {
  constructor(
    public id?: number,
    public name?: string,
    public dateOfIssue?: dayjs.Dayjs,
    public purchases?: IPurchase[] | null,
    public authors?: IAuthor[] | null
  ) {}
}

export function getBookIdentifier(book: IBook): number | undefined {
  return book.id;
}
