import { IPresentacion, NewPresentacion } from './presentacion.model';

export const sampleWithRequiredData: IPresentacion = {
  id: 31093,
};

export const sampleWithPartialData: IPresentacion = {
  id: 15084,
};

export const sampleWithFullData: IPresentacion = {
  id: 11644,
  descripcion: 'stained',
};

export const sampleWithNewData: NewPresentacion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
