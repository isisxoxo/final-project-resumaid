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
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatListModule } from '@angular/material/list';
import { MatStepperModule } from '@angular/material/stepper';
import { MatSortModule} from '@angular/material/sort';
import { DateAdapter } from '@angular/material/core';

const matModules: any[] = [
  MatMenuModule, MatIconModule,
  MatFormFieldModule, MatInputModule, MatCardModule,
  MatDialogModule, MatTabsModule, MatProgressSpinnerModule,
  MatTooltipModule, MatToolbarModule, MatButtonModule,
  MatGridListModule, MatCheckboxModule, MatDatepickerModule,
  MatNativeDateModule, MatPaginatorModule, MatListModule,
  MatStepperModule, MatSortModule
]

@NgModule({
  imports: [matModules],
  exports: [matModules]
})
export class MaterialModule { 

  constructor(private dateAdapter: DateAdapter<Date>) {
      this.dateAdapter.setLocale('en-GB'); //dd/MM/yyyy
  }
}
