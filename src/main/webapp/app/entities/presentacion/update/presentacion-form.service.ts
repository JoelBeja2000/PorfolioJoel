import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPresentacion, NewPresentacion } from '../presentacion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPresentacion for edit and NewPresentacionFormGroupInput for create.
 */
type PresentacionFormGroupInput = IPresentacion | PartialWithRequiredKeyOf<NewPresentacion>;

type PresentacionFormDefaults = Pick<NewPresentacion, 'id'>;

type PresentacionFormGroupContent = {
  id: FormControl<IPresentacion['id'] | NewPresentacion['id']>;
  descripcion: FormControl<IPresentacion['descripcion']>;
};

export type PresentacionFormGroup = FormGroup<PresentacionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PresentacionFormService {
  createPresentacionFormGroup(presentacion: PresentacionFormGroupInput = { id: null }): PresentacionFormGroup {
    const presentacionRawValue = {
      ...this.getFormDefaults(),
      ...presentacion,
    };
    return new FormGroup<PresentacionFormGroupContent>({
      id: new FormControl(
        { value: presentacionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      descripcion: new FormControl(presentacionRawValue.descripcion),
    });
  }

  getPresentacion(form: PresentacionFormGroup): IPresentacion | NewPresentacion {
    return form.getRawValue() as IPresentacion | NewPresentacion;
  }

  resetForm(form: PresentacionFormGroup, presentacion: PresentacionFormGroupInput): void {
    const presentacionRawValue = { ...this.getFormDefaults(), ...presentacion };
    form.reset(
      {
        ...presentacionRawValue,
        id: { value: presentacionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PresentacionFormDefaults {
    return {
      id: null,
    };
  }
}
