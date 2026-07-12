import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { IncidentForm } from './incident-form';

describe('IncidentForm', () => {
  let component: IncidentForm;
  let fixture: ComponentFixture<IncidentForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncidentForm],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { paramMap: { get: () => null } }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(IncidentForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});