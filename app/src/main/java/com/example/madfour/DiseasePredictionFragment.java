package com.example.madfour;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DiseasePredictionFragment extends Fragment {

    private Spinner spinner1, spinner2, spinner3;
    private Button predictButton;
    private TextView resultText, medicationText, precautionText;
    private List<String> allSymptoms;
    private List<Integer> symptomArray;

    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_prediction, container, false);

        // Initialize UI components
        spinner1 = view.findViewById(R.id.spinner1);
        spinner2 = view.findViewById(R.id.spinner2);
        spinner3 = view.findViewById(R.id.spinner3);
        predictButton = view.findViewById(R.id.predictButton);
        resultText = view.findViewById(R.id.resultText);
        medicationText = view.findViewById(R.id.medicationText);
        precautionText = view.findViewById(R.id.precautionText);

        // Initialize symptom list and array
        allSymptoms = new ArrayList<>(Arrays.asList(
                "itching",
                "skin_rash",
                "nodal_skin_eruptions",
                "continuous_sneezing",
                "shivering",
                "chills",
                "joint_pain",
                "stomach_pain",
                "acidity",
                "ulcers_on_tongue",
                "muscle_wasting",
                "vomiting",
                "burning_micturition",
                "spotting_urination",
                "fatigue",
                "weight_gain",
                "anxiety",
                "cold_hands_and_feet",
                "mood_swings",
                "weight_loss",
                "restlessness",
                "lethargy",
                "patches_in_throat",
                "irregular_sugar_level",
                "cough",
                "high_fever",
                "sunken_eyes",
                "breathlessness",
                "sweating",
                "dehydration",
                "indigestion",
                "headache",
                "yellowish_skin",
                "dark_urine",
                "nausea",
                "loss_of_appetite",
                "pain_behind_the_eyes",
                "back_pain",
                "constipation",
                "abdominal_pain",
                "diarrhoea",
                "mild_fever",
                "yellow_urine",
                "yellowing_of_eyes",
                "acute_liver_failure",
                "fluid_overload",
                "swelling_of_stomach",
                "swelled_lymph_nodes",
                "malaise",
                "blurred_and_distorted_vision",
                "phlegm",
                "throat_irritation",
                "redness_of_eyes",
                "sinus_pressure",
                "runny_nose",
                "congestion",
                "chest_pain",
                "weakness_in_limbs",
                "fast_heart_rate",
                "pain_during_bowel_movements",
                "pain_in_anal_region",
                "bloody_stool",
                "irritation_in_anus",
                "neck_pain",
                "dizziness",
                "cramps",
                "bruising",
                "obesity",
                "swollen_legs",
                "puffy_face_and_eyes",
                "enlarged_thyroid",
                "brittle_nails",
                "swollen_extremities",
                "excessive_hunger",
                "extra_marital_contacts",
                "drying_and_tingling_lips",
                "slurred_speech",
                "knee_pain",
                "hip_joint_pain",
                "muscle_weakness",
                "stiff_neck",
                "swelling_joints",
                "movement_stiffness",
                "spinning_movements",
                "loss_of_balance",
                "unsteadiness",
                "weakness_of_one_body_side",
                "loss_of_smell",
                "bladder_discomfort",
                "foul_smell_of_urine",
                "continuous_feel_of_urine",
                "passage_of_gases",
                "internal_itching",
                "toxic_look_(typhos)",
                "depression",
                "irritability",
                "muscle_pain",
                "altered_sensorium",
                "red_spots_over_body",
                "belly_pain",
                "abnormal_menstruation",
                "dischromic_patches",
                "watering_from_eyes",
                "increased_appetite",
                "polyuria",
                "family_history",
                "mucoid_sputum",
                "rusty_sputum",
                "lack_of_concentration",
                "visual_disturbances",
                "receiving_blood_transfusion",
                "receiving_unsterile_injections",
                "coma",
                "stomach_bleeding",
                "distention_of_abdomen",
                "history_of_alcohol_consumption",
                "fluid_overload",
                "blood_in_sputum",
                "prominent_veins_on_calf",
                "palpitations",
                "painful_walking",
                "pus_filled_pimples",
                "blackheads",
                "scurring",
                "skin_peeling",
                "silver_like_dusting",
                "small_dents_in_nails",
                "inflammatory_nails",
                "blister",
                "red_sore_around_nose",
                "yellow_crust_ooze"
        ));
        symptomArray = new ArrayList<>(Collections.nCopies(132, 0));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, allSymptoms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner3.setAdapter(adapter);

        setupSpinnerListener(spinner1);
        setupSpinnerListener(spinner2);
        setupSpinnerListener(spinner3);

        predictButton.setOnClickListener(v -> predictDisease());

        // Initialize Retrofit
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://medicineprediction-nyvl.onrender.com/") // Replace with actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        apiService = retrofit.create(ApiService.class);

        return view;
    }

    private void setupSpinnerListener(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSymptom = parent.getItemAtPosition(position).toString();
                updateSymptomArray(selectedSymptom, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void updateSymptomArray(String symptom, boolean isSelected) {
        int index = allSymptoms.indexOf(symptom);
        if (index != -1) {
            symptomArray.set(index, isSelected ? 1 : 0);
        }
    }

    private void predictDisease() {
        validateSymptomArray();

        SymptomRequest request = new SymptomRequest(symptomArray); // Create the request body
        apiService.predictDisease(request).enqueue(new Callback<DiseaseResponse>() {
            @Override
            public void onResponse(Call<DiseaseResponse> call, Response<DiseaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DiseaseResponse diseaseResponse = response.body();
                    String predictedDisease = diseaseResponse.getDisease();
                    resultText.setText("Predicted Disease: " + predictedDisease);

                    // Fetch medications and precautions locally
                    displayMedicationsAndPrecautions(predictedDisease);
                    Toast.makeText(getContext(), "API call Success " , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error predicting disease: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DiseaseResponse> call, Throwable t) {
                Toast.makeText(getContext(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateSymptomArray() {
        if (symptomArray.size() != 132) {
            Toast.makeText(getContext(), "Symptom array size is incorrect", Toast.LENGTH_SHORT).show();
        } else if (symptomArray.contains(null)) {
            Toast.makeText(getContext(), "Symptom array contains null values", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Disease Predicted", Toast.LENGTH_SHORT).show();
        }
    }
    private void displayMedicationsAndPrecautions(String disease) {
        String medications = "";
        String precautions = "";

        switch (disease.toLowerCase()) {
            case "fungal infection":
                medications = "Antifungal Cream, Fluconazole, Terbinafine, Clotrimazole, Ketoconazole";
                precautions = "Bath twice, Use detol or neem in bathing water, Keep infected area dry, Use clean cloths";
                break;
            case "allergy":
                medications = "Antihistamines, Decongestants, Epinephrine, Corticosteroids, Immunotherapy";
                precautions = "Apply calamine, Cover area with bandage, Use ice to compress itching";
                break;
            case "gerd":
                medications = "Proton Pump Inhibitors (PPIs), H2 Blockers, Antacids, Prokinetics, Antibiotics";
                precautions = "Avoid fatty spicy food, Avoid lying down after eating, Maintain healthy weight, Exercise";
                break;
            case "chronic cholestasis":
                medications = "Ursodeoxycholic acid, Cholestyramine, Methotrexate, Corticosteroids, Liver transplant";
                precautions = "Cold baths, Anti itch medicine, Consult doctor, Eat healthy";
                break;
            case "drug reaction":
                medications = "Antihistamines, Epinephrine, Corticosteroids, Antibiotics, Antifungal Cream";
                precautions = "Stop irritation, Consult nearest hospital, Stop taking drug, Follow up";
                break;
            case "malaria":
                medications = "Antimalarial drugs, Antipyretics, Antiemetic drugs, IV fluids, Blood transfusions";
                precautions = "Consult nearest hospital, Avoid oily food, Avoid non veg food, Keep mosquitos out";
                break;
            case "hypothyroidism":
                medications = "Levothyroxine, Antithyroid medications, Radioactive iodine, Thyroid surgery, Beta-blockers";
                precautions = "Reduce stress, Exercise, Eat healthy, Get proper sleep";
                break;
            case "psoriasis":
                medications = "Topical treatments, Phototherapy, Systemic medications, Biologics, Coal tar";
                precautions = "Wash hands with warm soapy water, Stop bleeding using pressure, Consult doctor, Salt baths";
                break;
            case "dengue":
                medications = "Antibiotics, Antipyretics, Analgesics, IV fluids, Corticosteroids";
                precautions = "Drink papaya leaf juice, Avoid fatty spicy food, Keep mosquitos away, Keep hydrated";
                break;
            case "hypertension":
                medications = "Antihypertensive medications, Diuretics, Beta-blockers, ACE inhibitors, Calcium channel blockers";
                precautions = "Meditation, Salt baths, Reduce stress, Get proper sleep";
                break;
            case "diabetes":
                medications = "Insulin, Metformin, Sulfonylureas, DPP-4 inhibitors, GLP-1 receptor agonists";
                precautions = "Have balanced diet, Exercise, Consult doctor, Follow up";
                break;
            case "hepatitis a":
                medications = "Vaccination, Antiviral drugs, IV fluids, Blood transfusions, Liver transplant";
                precautions = "Consult nearest hospital, Wash hands through, Avoid fatty spicy food, Medication";
                break;
            case "hepatitis b":
                medications = "Antiviral drugs, IV fluids, Blood transfusions, Platelet transfusions, Liver transplant";
                precautions = "Consult nearest hospital, Vaccination, Eat healthy, Medication";
                break;
            case "hepatitis c":
                medications = "Antiviral drugs, IV fluids, Blood transfusions, Platelet transfusions, Liver transplant";
                precautions = "Consult nearest hospital, Vaccination, Eat healthy, Medication";
                break;
            case "hepatitis d":
                medications = "Antiviral drugs, IV fluids, Blood transfusions, Platelet transfusions, Liver transplant";
                precautions = "Consult doctor, Medication, Eat healthy, Follow up";
                break;
            case "hepatitis e":
                medications = "Alcohol cessation, Corticosteroids, IV fluids, Liver transplant, Nutritional support";
                precautions = "Stop alcohol consumption, Rest, Consult doctor, Medication";
                break;
            case "alcoholic hepatitis":
                medications = "Antibiotics, Isoniazid, Rifampin, Ethambutol, Pyrazinamide";
                precautions = "Stop alcohol consumption, Consult doctor, Medication, Follow up";
                break;
            case "tuberculosis":
                medications = "Antipyretics, Decongestants, Cough suppressants, Antihistamines, Pain relievers";
                precautions = "Cover mouth, Consult doctor, Medication, Rest";
                break;
            case "common cold":
                medications = "Antibiotics, Antiviral drugs, Antifungal drugs, IV fluids, Oxygen therapy";
                precautions = "Drink vitamin c rich drinks, Take vapour, Avoid cold food, Keep fever in check";
                break;
            case "pneumonia":
                medications = "Laxatives, Pain relievers, Warm baths, Cold compresses, High-fiber diet";
                precautions = "Consult doctor, Medication, Rest, Follow up";
                break;
            case "dimorphic hemmorhoids(piles)":
                medications = "Nitroglycerin, Aspirin, Beta-blockers, Calcium channel blockers, Thrombolytic drugs";
                precautions = "Avoid fatty spicy food, Consume witch hazel, Warm bath with epsom salt, Consume alovera juice";
                break;
            case "heart attack":
                medications = "Compression stockings, Exercise, Elevating the legs, Sclerotherapy, Laser treatments";
                precautions = "Call ambulance, Chew or swallow asprin, Keep calm";
                break;
            case "varicose veins":
                medications = "Levothyroxine, Antithyroid medications, Beta-blockers, Radioactive iodine, Thyroid surgery";
                precautions = "Lie down flat and raise the leg high, Use oinments, Use vein compression, Dont stand still for long";
                break;
            case "hyperthyroidism":
                medications = "Antithyroid medications, Radioactive iodine, Thyroid surgery, Beta-blockers, Corticosteroids";
                precautions = "Eat healthy, Massage, Use lemon balm, Take radioactive iodine treatment";
                break;
            case "hypoglycemia":
                medications = "Glucose tablets, Candy or juice, Glucagon injection, IV dextrose, Diazoxide";
                precautions = "Lie down on side, Check in pulse, Drink sugary drinks, Consult doctor";
                break;
            case "osteoarthristis":
                medications = "NSAIDs, Disease-modifying antirheumatic drugs (DMARDs), Biologics, Corticosteroids, Joint replacement surgery";
                precautions = "Acetaminophen, Consult nearest hospital, Follow up, Salt baths";
                break;
            case "arthritis":
                medications = "Pain relievers, Exercise, Hot and cold packs, Joint protection, Physical therapy";
                precautions = "Exercise, Use hot and cold therapy, Try acupuncture, Massage";
                break;
            case "(vertigo) paroymsal positional vertigo":
                medications = "Vestibular rehabilitation, Canalith repositioning, Medications for nausea, Surgery, Home exercises";
                precautions = "Lie down, Avoid sudden change in body, Avoid abrupt head movement, Relax";
                break;
            case "acne":
                medications = "Topical treatments, Antibiotics, Oral medications, Hormonal treatments, Isotretinoin";
                precautions = "Bath twice, Avoid fatty spicy food, Drink plenty of water, Avoid too many products";
                break;
            case "urinary tract infection":
                medications = "Antibiotics, Urinary analgesics, Phenazopyridine, Antispasmodics, Probiotics";
                precautions = "Drink plenty of water, Increase vitamin c intake, Drink cranberry juice, Take probiotics";
                break;
            case "impetigo":
                medications = "Topical antibiotics, Oral antibiotics, Antiseptics, Ointments, Warm compresses";
                precautions = "Soak affected area in warm water, Use antibiotics, Remove scabs with wet compressed cloth, Consult doctor";
                break;
            case "bronchial asthma":
                medications = "Bronchodilators, Inhaled corticosteroids, Leukotriene modifiers, Mast cell stabilizers, Anticholinergics";
                precautions = "Switch to loose clothing, Take deep breaths, Get away from trigger, Seek help";
                break;
            case "jaundice":
                medications = "IV fluids, Blood transfusions, Liver transplant, Medications for itching, Antiviral medications";
                precautions = "Drink plenty of water, Consume milk thistle, Eat fruits and high fibrous food, Medication";
                break;
            case "typhoid":
                medications = "Vaccination, Antiviral drugs, IV fluids, Blood transfusions, Liver transplant";
                precautions = "Eat high calorie vegetables, Antibiotic therapy, Consult doctor, Medication";
                break;
            case "paralysis (brain hemorrhage)":
                medications = "Blood thinners, Clot-dissolving medications, Anticonvulsants, Physical therapy, Occupational therapy";
                precautions = "Massage, Eat healthy, Exercise, Consult doctor";
                break;
            case "chicken pox":
                medications = "Antiviral drugs, Pain relievers, IV fluids, Blood transfusions, Platelet transfusions";
                precautions = "Use neem in bathing, Consume neem leaves, Take vaccine, Avoid public places";
                break;
            case "cervical spondylosis":
                medications = "Pain relievers, Muscle relaxants, Physical therapy, Neck braces, Corticosteroids";
                precautions = "Use heating pad or cold pack, Exercise, Take otc pain reliever, Consult doctor";
                break;
            case "osteoporosis":
                medications = "Bisphosphonates, Hormone therapy, Calcium and vitamin D supplements, Calcitonin, Parathyroid hormone";
                precautions = "Exercise, Eat calcium-rich foods, Avoid smoking, Limit alcohol intake";
                break;
            case "anemia":
                medications = "Iron supplements, Vitamin B12 injections, Folic acid supplements, Blood transfusions, Erythropoiesis-stimulating agents";
                precautions = "Eat iron-rich foods, Take vitamin C, Avoid tea and coffee with meals, Follow up with doctor";
                break;
            case "depression":
                medications = "Antidepressants, SSRIs, SNRIs, MAOIs, Therapy";
                precautions = "Exercise, Maintain a healthy diet, Get enough sleep, Seek support";
                break;
            case "anxiety":
                medications = "Benzodiazepines, SSRIs, SNRIs, Beta-blockers, Therapy";
                precautions = "Practice relaxation techniques, Avoid caffeine, Exercise regularly, Seek support";
                break;
            case "schizophrenia":
                medications = "Antipsychotics, Mood stabilizers, Antidepressants, Therapy, Hospitalization";
                precautions = "Take medications as prescribed, Avoid alcohol and drugs, Maintain a support system, Follow up with doctor";
                break;
            case "bipolar disorder":
                medications = "Mood stabilizers, Antipsychotics, Antidepressants, Therapy, Hospitalization";
                precautions = "Take medications as prescribed, Maintain a routine, Avoid alcohol and drugs, Seek support";
                break;
            case "adhd":
                medications = "Stimulants, Non-stimulants, Antidepressants, Therapy, Behavioral interventions";
                precautions = "Follow a routine, Break tasks into smaller steps, Use reminders, Seek support";
                break;
            case "autism":
                medications = "Antipsychotics, Antidepressants, Stimulants, Therapy, Behavioral interventions";
                precautions = "Create a structured environment, Use visual aids, Encourage communication, Seek support";
                break;
            case "ptsd":
                medications = "Antidepressants, SSRIs, SNRIs, Therapy, Prazosin";
                precautions = "Practice relaxation techniques, Avoid triggers, Seek support, Follow up with doctor";
                break;
            case "ocd":
                medications = "SSRIs, Antidepressants, Antipsychotics, Therapy, Behavioral interventions";
                precautions = "Practice relaxation techniques, Avoid triggers, Follow a routine, Seek support";
                break;
            case "insomnia":
                medications = "Sedatives, Antidepressants, Melatonin, Therapy, Behavioral interventions";
                precautions = "Maintain a sleep schedule, Avoid caffeine, Create a relaxing bedtime routine, Seek support";
                break;
            case "sleep apnea":
                medications = "CPAP, Oral appliances, Surgery, Weight loss, Positional therapy";
                precautions = "Maintain a healthy weight, Avoid alcohol, Sleep on your side, Follow up with doctor";
                break;
            case "narcolepsy":
                medications = "Stimulants, Antidepressants, Sodium oxybate, Therapy, Behavioral interventions";
                precautions = "Maintain a regular sleep schedule, Take short naps, Avoid alcohol, Seek support";
                break;
            case "restless legs syndrome":
                medications = "Dopamine agonists, Iron supplements, Anticonvulsants, Benzodiazepines, Opioids";
                precautions = "Exercise regularly, Avoid caffeine, Maintain a regular sleep schedule, Seek support";
                break;
            case "fibromyalgia":
                medications = "Pain relievers, Antidepressants, Anticonvulsants, Therapy, Exercise";
                precautions = "Exercise regularly, Maintain a healthy diet, Get enough sleep, Seek support";
                break;
            case "chronic fatigue syndrome":
                medications = "Pain relievers, Antidepressants, Anticonvulsants, Therapy, Exercise";
                precautions = "Pace yourself, Maintain a healthy diet, Get enough sleep, Seek support";
                break;
            case "multiple sclerosis":
                medications = "Disease-modifying therapies, Corticosteroids, Muscle relaxants, Pain relievers, Therapy";
                precautions = "Exercise regularly, Maintain a healthy diet, Get enough sleep, Seek support";
                break;
            case "parkinson's disease":
                medications = "Levodopa, Dopamine agonists, MAO-B inhibitors, Anticholinergics, Therapy";
                precautions = "Exercise regularly, Maintain a healthy diet, Get enough sleep, Seek support";
                break;
            case "alzheimer's disease":
                medications = "Cholinesterase inhibitors, Memantine, Antidepressants, Antipsychotics, Therapy";
                precautions = "Exercise regularly, Maintain a healthy diet, Get enough sleep, Seek support";
                break;
            case "dementia":
                medications = "Cholinesterase inhibitors, Memantine, Antidepressants, Antipsychotics, Therapy";
                precautions = "Exercise regularly, Maintain a healthy diet, Get enough sleep, Seek support";
                break;
            case "stroke":
                medications = "Blood thinners, Clot-dissolving medications, Antihypertensives, Statins, Therapy";
                precautions = "Exercise regularly, Maintain a healthy diet, Get enough sleep, Seek support";
                break;
            case "epilepsy":
                medications = "Antiepileptic drugs, Vagus nerve stimulation, Ketogenic diet, Surgery, Therapy";
                precautions = "Take medications as prescribed, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "migraine":
                medications = "Analgesics, Triptans, Ergotamine derivatives, Preventive medications, Biofeedback";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "cluster headaches":
                medications = "Triptans, Oxygen therapy, Preventive medications, Nerve blocks, Surgery";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "tension headaches":
                medications = "Pain relievers, Muscle relaxants, Antidepressants, Therapy, Biofeedback";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "sinus headaches":
                medications = "Decongestants, Antihistamines, Pain relievers, Nasal corticosteroids, Antibiotics";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "trigeminal neuralgia":
                medications = "Anticonvulsants, Antidepressants, Pain relievers, Surgery, Therapy";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "bell's palsy":
                medications = "Corticosteroids, Antiviral drugs, Pain relievers, Physical therapy, Surgery";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "guillain-barre syndrome":
                medications = "Plasmapheresis, IV immunoglobulin, Pain relievers, Physical therapy, Occupational therapy";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "myasthenia gravis":
                medications = "Anticholinesterase medications, Immunosuppressants, Corticosteroids, Plasmapheresis, IV immunoglobulin";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "amyotrophic lateral sclerosis":
                medications = "Riluzole, Edaravone, Pain relievers, Physical therapy, Occupational therapy";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "huntington's disease":
                medications = "Antipsychotics, Antidepressants, Mood stabilizers, Therapy, Occupational therapy";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "muscular dystrophy":
                medications = "Corticosteroids, Pain relievers, Physical therapy, Occupational therapy, Surgery";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "spinal muscular atrophy":
                medications = "Nusinersen, Pain relievers, Physical therapy, Occupational therapy, Surgery";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "cerebral palsy":
                medications = "Muscle relaxants, Anticonvulsants, Pain relievers, Physical therapy, Occupational therapy";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            case "spina bifida":
                medications = "Pain relievers, Physical therapy, Occupational therapy, Surgery, Assistive devices";
                precautions = "Practice relaxation techniques, Avoid triggers, Maintain a healthy lifestyle, Seek support";
                break;
            // Add cases for all 132 diseases here...
            default:
                medications = "Consult a physician";
                precautions = "Follow a healthy lifestyle";
                break;
        }

        medicationText.setText("Medications: " + medications);
        precautionText.setText("Precautions: " + precautions);
    }
}
