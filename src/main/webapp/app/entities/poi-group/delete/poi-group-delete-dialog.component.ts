import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPoiGroup } from '../poi-group.model';
import { PoiGroupService } from '../service/poi-group.service';

@Component({
  templateUrl: './poi-group-delete-dialog.component.html',
})
export class PoiGroupDeleteDialogComponent {
  poiGroup?: IPoiGroup;

  constructor(protected poiGroupService: PoiGroupService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.poiGroupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
