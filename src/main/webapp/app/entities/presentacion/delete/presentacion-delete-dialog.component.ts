import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPresentacion } from '../presentacion.model';
import { PresentacionService } from '../service/presentacion.service';

@Component({
  standalone: true,
  templateUrl: './presentacion-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PresentacionDeleteDialogComponent {
  presentacion?: IPresentacion;

  protected presentacionService = inject(PresentacionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.presentacionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
