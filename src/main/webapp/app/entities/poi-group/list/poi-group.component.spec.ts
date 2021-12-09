import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PoiGroupService } from '../service/poi-group.service';

import { PoiGroupComponent } from './poi-group.component';

describe('PoiGroup Management Component', () => {
  let comp: PoiGroupComponent;
  let fixture: ComponentFixture<PoiGroupComponent>;
  let service: PoiGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PoiGroupComponent],
    })
      .overrideTemplate(PoiGroupComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PoiGroupComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PoiGroupService);

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
    expect(comp.poiGroups?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
