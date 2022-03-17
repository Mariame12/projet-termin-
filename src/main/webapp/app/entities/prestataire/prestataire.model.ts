import { IConsultant } from 'app/entities/consultant/consultant.model';

export interface IPrestataire {
  id?: number;
  nomPres?: string | null;
  nomCont?: string | null;
  prenomCont?: string | null;
  email?: string | null;
  consultants?: IConsultant[] | null;
}

export class Prestataire implements IPrestataire {
  constructor(
    public id?: number,
    public nomPres?: string | null,
    public nomCont?: string | null,
    public prenomCont?: string | null,
    public email?: string | null,
    public consultants?: IConsultant[] | null
  ) {}
}

export function getPrestataireIdentifier(prestataire: IPrestataire): number | undefined {
  return prestataire.id;
}
