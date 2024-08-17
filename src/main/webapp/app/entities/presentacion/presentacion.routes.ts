import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PresentacionComponent } from './list/presentacion.component';
import { PresentacionDetailComponent } from './detail/presentacion-detail.component';
import { PresentacionUpdateComponent } from './update/presentacion-update.component';
import PresentacionResolve from './route/presentacion-routing-resolve.service';

const presentacionRoute: Routes = [
  {
    path: '',
    component: PresentacionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PresentacionDetailComponent,
    resolve: {
      presentacion: PresentacionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PresentacionUpdateComponent,
    resolve: {
      presentacion: PresentacionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PresentacionUpdateComponent,
    resolve: {
      presentacion: PresentacionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default presentacionRoute;
