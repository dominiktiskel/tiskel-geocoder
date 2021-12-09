import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISetting } from '../setting.model';
import { SettingService } from '../service/setting.service';
import { SettingDeleteDialogComponent } from '../delete/setting-delete-dialog.component';

@Component({
  selector: 'jhi-setting',
  templateUrl: './setting.component.html',
})
export class SettingComponent implements OnInit {
  settings?: ISetting[];
  isLoading = false;

  constructor(protected settingService: SettingService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.settingService.query().subscribe(
      (res: HttpResponse<ISetting[]>) => {
        this.isLoading = false;
        this.settings = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISetting): number {
    return item.id!;
  }

  delete(setting: ISetting): void {
    const modalRef = this.modalService.open(SettingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.setting = setting;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
