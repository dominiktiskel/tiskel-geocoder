import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPoiGroup } from '../poi-group.model';
import { PoiGroupService } from '../service/poi-group.service';
import { PoiGroupDeleteDialogComponent } from '../delete/poi-group-delete-dialog.component';

@Component({
  selector: 'jhi-poi-group',
  templateUrl: './poi-group.component.html',
})
export class PoiGroupComponent implements OnInit {
  poiGroups?: IPoiGroup[];
  isLoading = false;

  constructor(protected poiGroupService: PoiGroupService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.poiGroupService.query().subscribe(
      (res: HttpResponse<IPoiGroup[]>) => {
        this.isLoading = false;
        this.poiGroups = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPoiGroup): number {
    return item.id!;
  }

  delete(poiGroup: IPoiGroup): void {
    const modalRef = this.modalService.open(PoiGroupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.poiGroup = poiGroup;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
