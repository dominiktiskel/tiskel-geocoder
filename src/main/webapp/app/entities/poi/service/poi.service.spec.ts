import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPoi, Poi } from '../poi.model';

import { PoiService } from './poi.service';

describe('Poi Service', () => {
  let service: PoiService;
  let httpMock: HttpTestingController;
  let elemDefault: IPoi;
  let expectedResult: IPoi | IPoi[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PoiService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      street: 'AAAAAAA',
      buildingNumber: 'AAAAAAA',
      postCode: 'AAAAAAA',
      lat: 0,
      lng: 0,
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

    it('should create a Poi', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Poi()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Poi', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          street: 'BBBBBB',
          buildingNumber: 'BBBBBB',
          postCode: 'BBBBBB',
          lat: 1,
          lng: 1,
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

    it('should partial update a Poi', () => {
      const patchObject = Object.assign(
        {
          street: 'BBBBBB',
          buildingNumber: 'BBBBBB',
          postCode: 'BBBBBB',
          lng: 1,
          active: true,
        },
        new Poi()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Poi', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          street: 'BBBBBB',
          buildingNumber: 'BBBBBB',
          postCode: 'BBBBBB',
          lat: 1,
          lng: 1,
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

    it('should delete a Poi', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPoiToCollectionIfMissing', () => {
      it('should add a Poi to an empty array', () => {
        const poi: IPoi = { id: 123 };
        expectedResult = service.addPoiToCollectionIfMissing([], poi);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(poi);
      });

      it('should not add a Poi to an array that contains it', () => {
        const poi: IPoi = { id: 123 };
        const poiCollection: IPoi[] = [
          {
            ...poi,
          },
          { id: 456 },
        ];
        expectedResult = service.addPoiToCollectionIfMissing(poiCollection, poi);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Poi to an array that doesn't contain it", () => {
        const poi: IPoi = { id: 123 };
        const poiCollection: IPoi[] = [{ id: 456 }];
        expectedResult = service.addPoiToCollectionIfMissing(poiCollection, poi);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(poi);
      });

      it('should add only unique Poi to an array', () => {
        const poiArray: IPoi[] = [{ id: 123 }, { id: 456 }, { id: 30309 }];
        const poiCollection: IPoi[] = [{ id: 123 }];
        expectedResult = service.addPoiToCollectionIfMissing(poiCollection, ...poiArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const poi: IPoi = { id: 123 };
        const poi2: IPoi = { id: 456 };
        expectedResult = service.addPoiToCollectionIfMissing([], poi, poi2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(poi);
        expect(expectedResult).toContain(poi2);
      });

      it('should accept null and undefined values', () => {
        const poi: IPoi = { id: 123 };
        expectedResult = service.addPoiToCollectionIfMissing([], null, poi, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(poi);
      });

      it('should return initial array if no Poi is added', () => {
        const poiCollection: IPoi[] = [{ id: 123 }];
        expectedResult = service.addPoiToCollectionIfMissing(poiCollection, undefined, null);
        expect(expectedResult).toEqual(poiCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
