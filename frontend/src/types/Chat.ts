import type {User} from './User';

export interface Chat {
  id: number;
  name: string | null;
  participants: User[];
}