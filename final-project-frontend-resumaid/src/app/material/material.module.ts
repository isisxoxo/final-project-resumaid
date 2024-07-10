import { NgModule } from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTabsModule } from '@angular/material/tabs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatToolbarModule } from '@angular/material/toolbar';
import {MatGridListModule} from '@angular/material/grid-list';

const matModules: any[] = [
  MatMenuModule, MatIconModule,
  MatFormFieldModule, MatInputModule, MatCardModule,
  MatDialogModule, MatTabsModule, MatProgressSpinnerModule,
  MatTooltipModule, MatToolbarModule, MatButtonModule,
  MatGridListModule
]

@NgModule({
  imports: [matModules],
  exports: [matModules]
})
export class MaterialModule { }
