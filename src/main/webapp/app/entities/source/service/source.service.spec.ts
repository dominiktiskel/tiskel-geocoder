import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISource, Source } from '../source.model';

import { SourceService } from './source.service';

describe('Source Service', () => {
  let service: SourceService;
  let httpMock: HttpTestingController;
  let elemDefault: ISource;
  let expectedResult: ISource | ISource[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SourceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a Source', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Source()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Source', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should partial update a Source', () => {
      const patchObject = Object.assign({}, new Source());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Source', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a Source', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSourceToCollectionIfMissing', () => {
      it('should add a Source to an empty array', () => {
        const source: ISource = { id: 123 };
        expectedResult = service.addSourceToCollectionIfMissing([], source);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(source);
      });

      it('should not add a Source to an array that contains it', () => {
        const source: ISource = { id: 123 };
        const sourceCollection: ISource[] = [
          {
            ...source,
          },
          { id: 456 },
        ];
        expectedResult = service.addSourceToCollectionIfMissing(sourceCollection, source);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Source to an array that doesn't contain it", () => {
        const source: ISource = { id: 123 };
        const sourceCollection: ISource[] = [{ id: 456 }];
        expectedResult = service.addSourceToCollectionIfMissing(sourceCollection, source);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(source);
      });

      it('should add only unique Source to an array', () => {
        const sourceArray: ISource[] = [{ id: 123 }, { id: 456 }, { id: 85198 }];
        const sourceCollection: ISource[] = [{ id: 123 }];
        expectedResult = service.addSourceToCollectionIfMissing(sourceCollection, ...sourceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const source: ISource = { id: 123 };
        const source2: ISource = { id: 456 };
        expectedResult = service.addSourceToCollectionIfMissing([], source, source2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(source);
        expect(expectedResult).toContain(source2);
      });

      it('should accept null and undefined values', () => {
        const source: ISource = { id: 123 };
        expectedResult = service.addSourceToCollectionIfMissing([], null, source, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(source);
      });

      it('should return initial array if no Source is added', () => {
        const sourceCollection: ISource[] = [{ id: 123 }];
        expectedResult = service.addSourceToCollectionIfMissing(sourceCollection, undefined, null);
        expect(expectedResult).toEqual(sourceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
