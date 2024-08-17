export interface IPersona {
  id: number;
  nombre?: string | null;
  apellido?: string | null;
  email?: string | null;
  telefono?: string | null;
}

export type NewPersona = Omit<IPersona, 'id'> & { id: null };
