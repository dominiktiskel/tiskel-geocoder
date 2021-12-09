import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPoiGroup, PoiGroup } from '../poi-group.model';
import { PoiGroupService } from '../service/poi-group.service';

@Component({
  selector: 'jhi-poi-group-update',
  templateUrl: './poi-group-update.component.html',
})
export class PoiGroupUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    prefix: [null, [Validators.required]],
    active: [null, [Validators.required]],
  });

  constructor(protected poiGroupService: PoiGroupService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ poiGroup }) => {
      this.updateForm(poiGroup);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const poiGroup = this.createFromForm();
    if (poiGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.poiGroupService.update(poiGroup));
    } else {
      this.subscribeToSaveResponse(this.poiGroupService.create(poiGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPoiGroup>>): void {
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

  protected updateForm(poiGroup: IPoiGroup): void {
    this.editForm.patchValue({
      id: poiGroup.id,
      name: poiGroup.name,
      prefix: poiGroup.prefix,
      active: poiGroup.active,
    });
  }

  protected createFromForm(): IPoiGroup {
    return {
      ...new PoiGroup(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      prefix: this.editForm.get(['prefix'])!.value,
      active: this.editForm.get(['active'])!.value,
    };
  }
}
