import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPoi } from '../poi.model';
import { PoiService } from '../service/poi.service';
import { PoiDeleteDialogComponent } from '../delete/poi-delete-dialog.component';

@Component({
  selector: 'jhi-poi',
  templateUrl: './poi.component.html',
})
export class PoiComponent implements OnInit {
  pois?: IPoi[];
  isLoading = false;

  constructor(protected poiService: PoiService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.poiService.query().subscribe(
      (res: HttpResponse<IPoi[]>) => {
        this.isLoading = false;
        this.pois = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPoi): number {
    return item.id!;
  }

  delete(poi: IPoi): void {
    const modalRef = this.modalService.open(PoiDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.poi = poi;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
