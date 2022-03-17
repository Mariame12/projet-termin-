import * as dayjs from 'dayjs';
import { IConsultant } from 'app/entities/consultant/consultant.model';

export interface IFichePresence {
  id?: number;
  activites?: string | null;
  heuredebut?: string | null;
  commentaire?: string | null;
  heurefin?: string | null;
  date?: dayjs.Dayjs | null;
  consultant?: IConsultant | null;
}

export class FichePresence implements IFichePresence {
  constructor(
    public id?: number,
    public activites?: string | null,
    public heuredebut?: string | null,
    public commentaire?: string | null,
    public heurefin?: string | null,
    public date?: dayjs.Dayjs | null,
    public consultant?: IConsultant | null
  ) {}
}

export function getFichePresenceIdentifier(fichePresence: IFichePresence): number | undefined {
  return fichePresence.id;
}
