import { IPrestataire } from 'app/entities/prestataire/prestataire.model';
import { IFichePresence } from 'app/entities/fiche-presence/fiche-presence.model';

export interface IConsultant {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  fonction?: string | null;
  prestataire?: IPrestataire | null;
  fichePresences?: IFichePresence[] | null;
}

export class Consultant implements IConsultant {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public fonction?: string | null,
    public prestataire?: IPrestataire | null,
    public fichePresences?: IFichePresence[] | null
  ) {}
}

export function getConsultantIdentifier(consultant: IConsultant): number | undefined {
  return consultant.id;
}
