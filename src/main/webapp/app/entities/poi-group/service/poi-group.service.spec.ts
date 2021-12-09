import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPoiGroup, PoiGroup } from '../poi-group.model';

import { PoiGroupService } from './poi-group.service';

describe('PoiGroup Service', () => {
  let service: PoiGroupService;
  let httpMock: HttpTestingController;
  let elemDefault: IPoiGroup;
  let expectedResult: IPoiGroup | IPoiGroup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PoiGroupService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      prefix: 'AAAAAAA',
      active: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PoiGroup', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PoiGroup()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PoiGroup', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          prefix: 'BBBBBB',
          active: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PoiGroup', () => {
      const patchObject = Object.assign({}, new PoiGroup());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PoiGroup', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          prefix: 'BBBBBB',
          active: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PoiGroup', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPoiGroupToCollectionIfMissing', () => {
      it('should add a PoiGroup to an empty array', () => {
        const poiGroup: IPoiGroup = { id: 123 };
        expectedResult = service.addPoiGroupToCollectionIfMissing([], poiGroup);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(poiGroup);
      });

      it('should not add a PoiGroup to an array that contains it', () => {
        const poiGroup: IPoiGroup = { id: 123 };
        const poiGroupCollection: IPoiGroup[] = [
          {
            ...poiGroup,
          },
          { id: 456 },
        ];
        expectedResult = service.addPoiGroupToCollectionIfMissing(poiGroupCollection, poiGroup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PoiGroup to an array that doesn't contain it", () => {
        const poiGroup: IPoiGroup = { id: 123 };
        const poiGroupCollection: IPoiGroup[] = [{ id: 456 }];
        expectedResult = service.addPoiGroupToCollectionIfMissing(poiGroupCollection, poiGroup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(poiGroup);
      });

      it('should add only unique PoiGroup to an array', () => {
        const poiGroupArray: IPoiGroup[] = [{ id: 123 }, { id: 456 }, { id: 38451 }];
        const poiGroupCollection: IPoiGroup[] = [{ id: 123 }];
        expectedResult = service.addPoiGroupToCollectionIfMissing(poiGroupCollection, ...poiGroupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const poiGroup: IPoiGroup = { id: 123 };
        const poiGroup2: IPoiGroup = { id: 456 };
        expectedResult = service.addPoiGroupToCollectionIfMissing([], poiGroup, poiGroup2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(poiGroup);
        expect(expectedResult).toContain(poiGroup2);
      });

      it('should accept null and undefined values', () => {
        const poiGroup: IPoiGroup = { id: 123 };
        expectedResult = service.addPoiGroupToCollectionIfMissing([], null, poiGroup, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(poiGroup);
      });

      it('should return initial array if no PoiGroup is added', () => {
        const poiGroupCollection: IPoiGroup[] = [{ id: 123 }];
        expectedResult = service.addPoiGroupToCollectionIfMissing(poiGroupCollection, undefined, null);
        expect(expectedResult).toEqual(poiGroupCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
