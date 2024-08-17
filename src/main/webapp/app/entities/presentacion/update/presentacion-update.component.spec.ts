import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { PresentacionService } from '../service/presentacion.service';
import { IPresentacion } from '../presentacion.model';
import { PresentacionFormService } from './presentacion-form.service';

import { PresentacionUpdateComponent } from './presentacion-update.component';

describe('Presentacion Management Update Component', () => {
  let comp: PresentacionUpdateComponent;
  let fixture: ComponentFixture<PresentacionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let presentacionFormService: PresentacionFormService;
  let presentacionService: PresentacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PresentacionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PresentacionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PresentacionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    presentacionFormService = TestBed.inject(PresentacionFormService);
    presentacionService = TestBed.inject(PresentacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const presentacion: IPresentacion = { id: 456 };

      activatedRoute.data = of({ presentacion });
      comp.ngOnInit();

      expect(comp.presentacion).toEqual(presentacion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPresentacion>>();
      const presentacion = { id: 123 };
      jest.spyOn(presentacionFormService, 'getPresentacion').mockReturnValue(presentacion);
      jest.spyOn(presentacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ presentacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: presentacion }));
      saveSubject.complete();

      // THEN
      expect(presentacionFormService.getPresentacion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(presentacionService.update).toHaveBeenCalledWith(expect.objectContaining(presentacion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPresentacion>>();
      const presentacion = { id: 123 };
      jest.spyOn(presentacionFormService, 'getPresentacion').mockReturnValue({ id: null });
      jest.spyOn(presentacionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ presentacion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: presentacion }));
      saveSubject.complete();

      // THEN
      expect(presentacionFormService.getPresentacion).toHaveBeenCalled();
      expect(presentacionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPresentacion>>();
      const presentacion = { id: 123 };
      jest.spyOn(presentacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ presentacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(presentacionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
