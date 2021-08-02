import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPurchase, getPurchaseIdentifier } from '../purchase.model';

export type EntityResponseType = HttpResponse<IPurchase>;
export type EntityArrayResponseType = HttpResponse<IPurchase[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/purchases');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(purchase: IPurchase): Observable<EntityResponseType> {
    return this.http.post<IPurchase>(this.resourceUrl, purchase, { observe: 'response' });
  }

  update(purchase: IPurchase): Observable<EntityResponseType> {
    return this.http.put<IPurchase>(`${this.resourceUrl}/${getPurchaseIdentifier(purchase) as number}`, purchase, { observe: 'response' });
  }

  partialUpdate(purchase: IPurchase): Observable<EntityResponseType> {
    return this.http.patch<IPurchase>(`${this.resourceUrl}/${getPurchaseIdentifier(purchase) as number}`, purchase, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPurchase>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPurchase[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPurchaseToCollectionIfMissing(purchaseCollection: IPurchase[], ...purchasesToCheck: (IPurchase | null | undefined)[]): IPurchase[] {
    const purchases: IPurchase[] = purchasesToCheck.filter(isPresent);
    if (purchases.length > 0) {
      const purchaseCollectionIdentifiers = purchaseCollection.map(purchaseItem => getPurchaseIdentifier(purchaseItem)!);
      const purchasesToAdd = purchases.filter(purchaseItem => {
        const purchaseIdentifier = getPurchaseIdentifier(purchaseItem);
        if (purchaseIdentifier == null || purchaseCollectionIdentifiers.includes(purchaseIdentifier)) {
          return false;
        }
        purchaseCollectionIdentifiers.push(purchaseIdentifier);
        return true;
      });
      return [...purchasesToAdd, ...purchaseCollection];
    }
    return purchaseCollection;
  }
}
