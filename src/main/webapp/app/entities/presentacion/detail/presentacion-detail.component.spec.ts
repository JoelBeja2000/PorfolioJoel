import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PresentacionDetailComponent } from './presentacion-detail.component';

describe('Presentacion Management Detail Component', () => {
  let comp: PresentacionDetailComponent;
  let fixture: ComponentFixture<PresentacionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PresentacionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PresentacionDetailComponent,
              resolve: { presentacion: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PresentacionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PresentacionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load presentacion on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PresentacionDetailComponent);

      // THEN
      expect(instance.presentacion()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
