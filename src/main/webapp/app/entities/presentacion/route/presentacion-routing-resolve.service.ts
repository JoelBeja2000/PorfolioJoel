import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPresentacion } from '../presentacion.model';
import { PresentacionService } from '../service/presentacion.service';

const presentacionResolve = (route: ActivatedRouteSnapshot): Observable<null | IPresentacion> => {
  const id = route.params['id'];
  if (id) {
    return inject(PresentacionService)
      .find(id)
      .pipe(
        mergeMap((presentacion: HttpResponse<IPresentacion>) => {
          if (presentacion.body) {
            return of(presentacion.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default presentacionResolve;
