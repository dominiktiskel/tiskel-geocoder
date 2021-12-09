jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PoiGroupService } from '../service/poi-group.service';
import { IPoiGroup, PoiGroup } from '../poi-group.model';

import { PoiGroupUpdateComponent } from './poi-group-update.component';

describe('PoiGroup Management Update Component', () => {
  let comp: PoiGroupUpdateComponent;
  let fixture: ComponentFixture<PoiGroupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let poiGroupService: PoiGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PoiGroupUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PoiGroupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PoiGroupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    poiGroupService = TestBed.inject(PoiGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const poiGroup: IPoiGroup = { id: 456 };

      activatedRoute.data = of({ poiGroup });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(poiGroup));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PoiGroup>>();
      const poiGroup = { id: 123 };
      jest.spyOn(poiGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ poiGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: poiGroup }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(poiGroupService.update).toHaveBeenCalledWith(poiGroup);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PoiGroup>>();
      const poiGroup = new PoiGroup();
      jest.spyOn(poiGroupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ poiGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: poiGroup }));
      saveSubject.complete();

      // THEN
      expect(poiGroupService.create).toHaveBeenCalledWith(poiGroup);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PoiGroup>>();
      const poiGroup = { id: 123 };
      jest.spyOn(poiGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ poiGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(poiGroupService.update).toHaveBeenCalledWith(poiGroup);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
