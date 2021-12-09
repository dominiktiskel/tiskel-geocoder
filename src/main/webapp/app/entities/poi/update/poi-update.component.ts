import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPoi, Poi } from '../poi.model';
import { PoiService } from '../service/poi.service';
import { ISource } from 'app/entities/source/source.model';
import { SourceService } from 'app/entities/source/service/source.service';
import { IPoiGroup } from 'app/entities/poi-group/poi-group.model';
import { PoiGroupService } from 'app/entities/poi-group/service/poi-group.service';

@Component({
  selector: 'jhi-poi-update',
  templateUrl: './poi-update.component.html',
})
export class PoiUpdateComponent implements OnInit {
  isSaving = false;

  sourcesSharedCollection: ISource[] = [];
  poiGroupsSharedCollection: IPoiGroup[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    street: [],
    buildingNumber: [],
    postCode: [],
    lat: [null, [Validators.required]],
    lng: [null, [Validators.required]],
    active: [null, [Validators.required]],
    source: [null, Validators.required],
    poiGroup: [null, Validators.required],
  });

  constructor(
    protected poiService: PoiService,
    protected sourceService: SourceService,
    protected poiGroupService: PoiGroupService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ poi }) => {
      this.updateForm(poi);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const poi = this.createFromForm();
    if (poi.id !== undefined) {
      this.subscribeToSaveResponse(this.poiService.update(poi));
    } else {
      this.subscribeToSaveResponse(this.poiService.create(poi));
    }
  }

  trackSourceById(index: number, item: ISource): number {
    return item.id!;
  }

  trackPoiGroupById(index: number, item: IPoiGroup): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPoi>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
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

  protected updateForm(poi: IPoi): void {
    this.editForm.patchValue({
      id: poi.id,
      name: poi.name,
      street: poi.street,
      buildingNumber: poi.buildingNumber,
      postCode: poi.postCode,
      lat: poi.lat,
      lng: poi.lng,
      active: poi.active,
      source: poi.source,
      poiGroup: poi.poiGroup,
    });

    this.sourcesSharedCollection = this.sourceService.addSourceToCollectionIfMissing(this.sourcesSharedCollection, poi.source);
    this.poiGroupsSharedCollection = this.poiGroupService.addPoiGroupToCollectionIfMissing(this.poiGroupsSharedCollection, poi.poiGroup);
  }

  protected loadRelationshipsOptions(): void {
    this.sourceService
      .query()
      .pipe(map((res: HttpResponse<ISource[]>) => res.body ?? []))
      .pipe(map((sources: ISource[]) => this.sourceService.addSourceToCollectionIfMissing(sources, this.editForm.get('source')!.value)))
      .subscribe((sources: ISource[]) => (this.sourcesSharedCollection = sources));

    this.poiGroupService
      .query()
      .pipe(map((res: HttpResponse<IPoiGroup[]>) => res.body ?? []))
      .pipe(
        map((poiGroups: IPoiGroup[]) =>
          this.poiGroupService.addPoiGroupToCollectionIfMissing(poiGroups, this.editForm.get('poiGroup')!.value)
        )
      )
      .subscribe((poiGroups: IPoiGroup[]) => (this.poiGroupsSharedCollection = poiGroups));
  }

  protected createFromForm(): IPoi {
    return {
      ...new Poi(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      street: this.editForm.get(['street'])!.value,
      buildingNumber: this.editForm.get(['buildingNumber'])!.value,
      postCode: this.editForm.get(['postCode'])!.value,
      lat: this.editForm.get(['lat'])!.value,
      lng: this.editForm.get(['lng'])!.value,
      active: this.editForm.get(['active'])!.value,
      source: this.editForm.get(['source'])!.value,
      poiGroup: this.editForm.get(['poiGroup'])!.value,
    };
  }
}
