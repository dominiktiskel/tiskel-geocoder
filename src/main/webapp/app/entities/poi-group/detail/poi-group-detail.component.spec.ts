import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoiGroupDetailComponent } from './poi-group-detail.component';

describe('PoiGroup Management Detail Component', () => {
  let comp: PoiGroupDetailComponent;
  let fixture: ComponentFixture<PoiGroupDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PoiGroupDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ poiGroup: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PoiGroupDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PoiGroupDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load poiGroup on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.poiGroup).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
