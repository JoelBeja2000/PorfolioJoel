import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPresentacion } from '../presentacion.model';
import { PresentacionService } from '../service/presentacion.service';
import { PresentacionFormService, PresentacionFormGroup } from './presentacion-form.service';

@Component({
  standalone: true,
  selector: 'jhi-presentacion-update',
  templateUrl: './presentacion-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PresentacionUpdateComponent implements OnInit {
  isSaving = false;
  presentacion: IPresentacion | null = null;

  protected presentacionService = inject(PresentacionService);
  protected presentacionFormService = inject(PresentacionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PresentacionFormGroup = this.presentacionFormService.createPresentacionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ presentacion }) => {
      this.presentacion = presentacion;
      if (presentacion) {
        this.updateForm(presentacion);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const presentacion = this.presentacionFormService.getPresentacion(this.editForm);
    if (presentacion.id !== null) {
      this.subscribeToSaveResponse(this.presentacionService.update(presentacion));
    } else {
      this.subscribeToSaveResponse(this.presentacionService.create(presentacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPresentacion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(presentacion: IPresentacion): void {
    this.presentacion = presentacion;
    this.presentacionFormService.resetForm(this.editForm, presentacion);
  }
}
