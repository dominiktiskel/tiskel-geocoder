import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPoi } from '../poi.model';

@Component({
  selector: 'jhi-poi-detail',
  templateUrl: './poi-detail.component.html',
})
export class PoiDetailComponent implements OnInit {
  poi: IPoi | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ poi }) => {
      this.poi = poi;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
