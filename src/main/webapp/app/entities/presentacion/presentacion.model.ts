export interface IPresentacion {
  id: number;
  descripcion?: string | null;
}

export type NewPresentacion = Omit<IPresentacion, 'id'> & { id: null };
