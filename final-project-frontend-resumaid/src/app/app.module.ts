import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { RouterModule, Routes } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { UserService } from './services/user.service';
import { MainComponent } from './components/main/main.component';
import { JwtService } from './services/jwt.service';
import { jwtInterceptor } from './jwt.interceptor';
import { enterRestricted } from './route.guards';
import { CreateComponent } from './components/create/create.component';
import { ResumebuilderComponent } from './components/create/resumebuilder.component';
import { ResumepreviewComponent } from './components/create/resumepreview.component';
import { ResumeService } from './services/resume.service';
import { ResumeStore } from './services/resume.store';
import { OllamaService } from './services/ollama.service';
import { OllamaComponent } from './components/create/ollama.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { DraftsComponent } from './drafts/drafts.component';
import { ImageStore } from './services/image.store';
import { ConsultationComponent } from './components/consultation/consultation.component';
import { ConsultationService } from './services/consultation.service';
import { MaterialModule } from './material/material.module';
import { StripeService } from './services/stripe.service';
import { PaymentComponent } from './components/payment/payment.component';
import { DownloadComponent } from './components/download/download.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { SuccessComponent } from './components/payment/success.component';
import { CancelComponent } from './components/payment/cancel.component';
import { BookingsComponent } from './bookings/bookings.component';
import { HomeComponent } from './home/home.component';
import { ToolbarComponent } from './toolbar/toolbar.component';

const appRoutes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'login', component: LoginComponent},
  {path: 'main/:id', component: MainComponent, canActivate: [enterRestricted]},
  {path: 'create/:id', component: CreateComponent, canActivate: [enterRestricted]},
  {path: 'view/:id', component: DraftsComponent, canActivate: [enterRestricted]},
  {path: 'consult/:id', component: ConsultationComponent, canActivate: [enterRestricted]},
  {path: 'payment/:id', component: PaymentComponent, canActivate: [enterRestricted]},
  {path: 'success/:id', component: SuccessComponent, canActivate: [enterRestricted]},
  {path: 'cancel/:id', component: CancelComponent, canActivate: [enterRestricted]},
  {path: 'download/:id', component: DownloadComponent, canActivate: [enterRestricted]},
  {path: 'bookings/:id', component: BookingsComponent, canActivate: [enterRestricted]},
  {path: '**', redirectTo:'/', pathMatch: 'full'}
]

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    MainComponent,
    CreateComponent,
    ResumebuilderComponent,
    ResumepreviewComponent,
    OllamaComponent,
    DraftsComponent,
    ConsultationComponent,
    PaymentComponent,
    DownloadComponent,
    SuccessComponent,
    CancelComponent,
    BookingsComponent,
    HomeComponent,
    ToolbarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    MaterialModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, {useHash: true}),
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: !isDevMode(),
      // Register the ServiceWorker as soon as the application is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    })
  ],
  providers: [UserService,
    JwtService, 
    ResumeService,
    ResumeStore,
    ImageStore,
    OllamaService,
    ConsultationService,
    StripeService,
    { provide: HTTP_INTERCEPTORS, useClass: jwtInterceptor, multi: true },
    provideAnimationsAsync() // All classes have this
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
