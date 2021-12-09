import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SettingService } from '../service/setting.service';

import { SettingComponent } from './setting.component';

describe('Setting Management Component', () => {
  let comp: SettingComponent;
  let fixture: ComponentFixture<SettingComponent>;
  let service: SettingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SettingComponent],
    })
      .overrideTemplate(SettingComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SettingComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SettingService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.settings?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
