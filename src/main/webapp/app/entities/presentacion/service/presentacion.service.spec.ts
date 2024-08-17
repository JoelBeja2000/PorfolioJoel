import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPresentacion } from '../presentacion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../presentacion.test-samples';

import { PresentacionService } from './presentacion.service';

const requireRestSample: IPresentacion = {
  ...sampleWithRequiredData,
};

describe('Presentacion Service', () => {
  let service: PresentacionService;
  let httpMock: HttpTestingController;
  let expectedResult: IPresentacion | IPresentacion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PresentacionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Presentacion', () => {
      const presentacion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(presentacion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Presentacion', () => {
      const presentacion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(presentacion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Presentacion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Presentacion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Presentacion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPresentacionToCollectionIfMissing', () => {
      it('should add a Presentacion to an empty array', () => {
        const presentacion: IPresentacion = sampleWithRequiredData;
        expectedResult = service.addPresentacionToCollectionIfMissing([], presentacion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(presentacion);
      });

      it('should not add a Presentacion to an array that contains it', () => {
        const presentacion: IPresentacion = sampleWithRequiredData;
        const presentacionCollection: IPresentacion[] = [
          {
            ...presentacion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPresentacionToCollectionIfMissing(presentacionCollection, presentacion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Presentacion to an array that doesn't contain it", () => {
        const presentacion: IPresentacion = sampleWithRequiredData;
        const presentacionCollection: IPresentacion[] = [sampleWithPartialData];
        expectedResult = service.addPresentacionToCollectionIfMissing(presentacionCollection, presentacion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(presentacion);
      });

      it('should add only unique Presentacion to an array', () => {
        const presentacionArray: IPresentacion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const presentacionCollection: IPresentacion[] = [sampleWithRequiredData];
        expectedResult = service.addPresentacionToCollectionIfMissing(presentacionCollection, ...presentacionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const presentacion: IPresentacion = sampleWithRequiredData;
        const presentacion2: IPresentacion = sampleWithPartialData;
        expectedResult = service.addPresentacionToCollectionIfMissing([], presentacion, presentacion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(presentacion);
        expect(expectedResult).toContain(presentacion2);
      });

      it('should accept null and undefined values', () => {
        const presentacion: IPresentacion = sampleWithRequiredData;
        expectedResult = service.addPresentacionToCollectionIfMissing([], null, presentacion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(presentacion);
      });

      it('should return initial array if no Presentacion is added', () => {
        const presentacionCollection: IPresentacion[] = [sampleWithRequiredData];
        expectedResult = service.addPresentacionToCollectionIfMissing(presentacionCollection, undefined, null);
        expect(expectedResult).toEqual(presentacionCollection);
      });
    });

    describe('comparePresentacion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePresentacion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePresentacion(entity1, entity2);
        const compareResult2 = service.comparePresentacion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePresentacion(entity1, entity2);
        const compareResult2 = service.comparePresentacion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePresentacion(entity1, entity2);
        const compareResult2 = service.comparePresentacion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
