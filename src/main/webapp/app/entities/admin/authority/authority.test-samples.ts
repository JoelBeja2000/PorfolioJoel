import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'f7e566de-aea0-4e2d-befb-8c098e537dbe',
};

export const sampleWithPartialData: IAuthority = {
  name: '1f8efe33-0728-4e26-a28f-60988a43639f',
};

export const sampleWithFullData: IAuthority = {
  name: '2695985e-e7d7-4c45-8150-74adf207cfc0',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
