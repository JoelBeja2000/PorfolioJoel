import { Component, inject, signal, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { StateStorageService } from 'app/core/auth/state-storage.service';
import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import ActiveMenuDirective from './active-menu.directive';
import NavbarItem from './navbar-item.model';

interface SidebarItem {
  type: 'button' | 'folder';
  name: string;
  icon: string;
  route?: string;
  children?: SidebarItem[];
}

@Component({
  standalone: true,
  selector: 'jhi-navbar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
  imports: [RouterModule, SharedModule, HasAnyAuthorityDirective, ActiveMenuDirective],
})
export default class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = signal(true);
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account = inject(AccountService).trackCurrentAccount();
  entitiesNavbarItems: NavbarItem[] = [];
  sidebarItems: SidebarItem[] = [];

  private loginService = inject(LoginService);
  private translateService = inject(TranslateService);
  private stateStorageService = inject(StateStorageService);
  private profileService = inject(ProfileService);
  private router = inject(Router);

  constructor() {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
    this.loadSidebarConfig();
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
      this.setBackgroundColor(this.generarColorHexadecimalAleatorio());
    });
  }

  // Función para cargar el archivo JSON
  async loadSidebarConfig(): Promise<SidebarItem[]> {
    const response = await fetch('sidebar-data.json');
    const data = await response.json();
    return data;
  }

  changeLanguage(languageKey: string): void {
    this.stateStorageService.storeLocale(languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed.set(true);
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed.update(isNavbarCollapsed => !isNavbarCollapsed);
  }

  generarColorHexadecimalAleatorio(): string {
    // Genera un número aleatorio entre 0 y 0xFFFFFF (16777215 en decimal)
    const colorAleatorio = Math.floor(Math.random() * 0xffffff);

    // Convierte el número a una cadena hexadecimal y asegura que tenga 6 caracteres
    const colorHex = colorAleatorio.toString(16).padStart(6, '0');

    // Devuelve el color en formato #RRGGBB
    return `#${colorHex}`;
  }

  hasItem(route: string): boolean {
    return this.sidebarItems.some(item => item.route === route);
  }

  getItemIcon(route: string): string {
    const item = this.sidebarItems.find(item => item.route === route);
    return item ? item.icon : '';
  }

  getItemName(route: string): string {
    const item = this.sidebarItems.find(item => item.route === route);
    return item ? item.name : '';
  }

  private setBackgroundColor(color: string): void {
    document.documentElement.style.setProperty('--bg-color', color);
  }
}
