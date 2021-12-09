jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PoiService } from '../service/poi.service';
import { IPoi, Poi } from '../poi.model';
import { ISource } from 'app/entities/source/source.model';
import { SourceService } from 'app/entities/source/service/source.service';
import { IPoiGroup } from 'app/entities/poi-group/poi-group.model';
import { PoiGroupService } from 'app/entities/poi-group/service/poi-group.service';

import { PoiUpdateComponent } from './poi-update.component';

describe('Poi Management Update Component', () => {
  let comp: PoiUpdateComponent;
  let fixture: ComponentFixture<PoiUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let poiService: PoiService;
  let sourceService: SourceService;
  let poiGroupService: PoiGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PoiUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PoiUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PoiUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    poiService = TestBed.inject(PoiService);
    sourceService = TestBed.inject(SourceService);
    poiGroupService = TestBed.inject(PoiGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Source query and add missing value', () => {
      const poi: IPoi = { id: 456 };
      const source: ISource = { id: 2529 };
      poi.source = source;

      const sourceCollection: ISource[] = [{ id: 78469 }];
      jest.spyOn(sourceService, 'query').mockReturnValue(of(new HttpResponse({ body: sourceCollection })));
      const additionalSources = [source];
      const expectedCollection: ISource[] = [...additionalSources, ...sourceCollection];
      jest.spyOn(sourceService, 'addSourceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ poi });
      comp.ngOnInit();

      expect(sourceService.query).toHaveBeenCalled();
      expect(sourceService.addSourceToCollectionIfMissing).toHaveBeenCalledWith(sourceCollection, ...additionalSources);
      expect(comp.sourcesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PoiGroup query and add missing value', () => {
      const poi: IPoi = { id: 456 };
      const poiGroup: IPoiGroup = { id: 89048 };
      poi.poiGroup = poiGroup;

      const poiGroupCollection: IPoiGroup[] = [{ id: 1148 }];
      jest.spyOn(poiGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: poiGroupCollection })));
      const additionalPoiGroups = [poiGroup];
      const expectedCollection: IPoiGroup[] = [...additionalPoiGroups, ...poiGroupCollection];
      jest.spyOn(poiGroupService, 'addPoiGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ poi });
      comp.ngOnInit();

      expect(poiGroupService.query).toHaveBeenCalled();
      expect(poiGroupService.addPoiGroupToCollectionIfMissing).toHaveBeenCalledWith(poiGroupCollection, ...additionalPoiGroups);
      expect(comp.poiGroupsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const poi: IPoi = { id: 456 };
      const source: ISource = { id: 22584 };
      poi.source = source;
      const poiGroup: IPoiGroup = { id: 94038 };
      poi.poiGroup = poiGroup;

      activatedRoute.data = of({ poi });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(poi));
      expect(comp.sourcesSharedCollection).toContain(source);
      expect(comp.poiGroupsSharedCollection).toContain(poiGroup);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Poi>>();
      const poi = { id: 123 };
      jest.spyOn(poiService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ poi });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: poi }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(poiService.update).toHaveBeenCalledWith(poi);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Poi>>();
      const poi = new Poi();
      jest.spyOn(poiService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ poi });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: poi }));
      saveSubject.complete();

      // THEN
      expect(poiService.create).toHaveBeenCalledWith(poi);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Poi>>();
      const poi = { id: 123 };
      jest.spyOn(poiService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ poi });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(poiService.update).toHaveBeenCalledWith(poi);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSourceById', () => {
      it('Should return tracked Source primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSourceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPoiGroupById', () => {
      it('Should return tracked PoiGroup primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPoiGroupById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
