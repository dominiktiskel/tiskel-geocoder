import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPoi } from '../poi.model';
import { PoiService } from '../service/poi.service';

@Component({
  templateUrl: './poi-delete-dialog.component.html',
})
export class PoiDeleteDialogComponent {
  poi?: IPoi;

  constructor(protected poiService: PoiService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.poiService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
