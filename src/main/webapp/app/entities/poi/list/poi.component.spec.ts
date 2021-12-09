import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PoiService } from '../service/poi.service';

import { PoiComponent } from './poi.component';

describe('Poi Management Component', () => {
  let comp: PoiComponent;
  let fixture: ComponentFixture<PoiComponent>;
  let service: PoiService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PoiComponent],
    })
      .overrideTemplate(PoiComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PoiComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PoiService);

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
    expect(comp.pois?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
