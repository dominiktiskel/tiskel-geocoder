import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoiDetailComponent } from './poi-detail.component';

describe('Poi Management Detail Component', () => {
  let comp: PoiDetailComponent;
  let fixture: ComponentFixture<PoiDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PoiDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ poi: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PoiDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PoiDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load poi on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.poi).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
