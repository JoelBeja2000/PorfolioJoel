import { IPersona, NewPersona } from './persona.model';

export const sampleWithRequiredData: IPersona = {
  id: 21836,
};

export const sampleWithPartialData: IPersona = {
  id: 28254,
  nombre: 'huzzah among',
  apellido: 'esteem nonstop',
  email: 'MarcoAntonio16@gmail.com',
};

export const sampleWithFullData: IPersona = {
  id: 1065,
  nombre: 'hospitable um mockingly',
  apellido: 'speedily irrupt aha',
  email: 'Marilu_PalomoGuzman13@yahoo.com',
  telefono: 'since',
};

export const sampleWithNewData: NewPersona = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
