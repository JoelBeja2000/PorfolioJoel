import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../presentacion.test-samples';

import { PresentacionFormService } from './presentacion-form.service';

describe('Presentacion Form Service', () => {
  let service: PresentacionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PresentacionFormService);
  });

  describe('Service methods', () => {
    describe('createPresentacionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPresentacionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descripcion: expect.any(Object),
          }),
        );
      });

      it('passing IPresentacion should create a new form with FormGroup', () => {
        const formGroup = service.createPresentacionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descripcion: expect.any(Object),
          }),
        );
      });
    });

    describe('getPresentacion', () => {
      it('should return NewPresentacion for default Presentacion initial value', () => {
        const formGroup = service.createPresentacionFormGroup(sampleWithNewData);

        const presentacion = service.getPresentacion(formGroup) as any;

        expect(presentacion).toMatchObject(sampleWithNewData);
      });

      it('should return NewPresentacion for empty Presentacion initial value', () => {
        const formGroup = service.createPresentacionFormGroup();

        const presentacion = service.getPresentacion(formGroup) as any;

        expect(presentacion).toMatchObject({});
      });

      it('should return IPresentacion', () => {
        const formGroup = service.createPresentacionFormGroup(sampleWithRequiredData);

        const presentacion = service.getPresentacion(formGroup) as any;

        expect(presentacion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPresentacion should not enable id FormControl', () => {
        const formGroup = service.createPresentacionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPresentacion should disable id FormControl', () => {
        const formGroup = service.createPresentacionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
