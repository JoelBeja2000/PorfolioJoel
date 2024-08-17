import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'porfolioJoelApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'presentacion',
    data: { pageTitle: 'porfolioJoelApp.presentacion.home.title' },
    loadChildren: () => import('./presentacion/presentacion.routes'),
  },
  {
    path: 'persona',
    data: { pageTitle: 'porfolioJoelApp.persona.home.title' },
    loadChildren: () => import('./persona/persona.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
