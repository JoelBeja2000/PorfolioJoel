import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 25381,
  login: '}-@s6JcB\\2rifx60\\VJDdE\\.RY7-\\)Datj\\7wNfhDV',
};

export const sampleWithPartialData: IUser = {
  id: 30450,
  login: 'ZijOD',
};

export const sampleWithFullData: IUser = {
  id: 2244,
  login: 'yHfl6',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
