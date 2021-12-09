import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPoiGroup } from '../poi-group.model';

@Component({
  selector: 'jhi-poi-group-detail',
  templateUrl: './poi-group-detail.component.html',
})
export class PoiGroupDetailComponent implements OnInit {
  poiGroup: IPoiGroup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ poiGroup }) => {
      this.poiGroup = poiGroup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
