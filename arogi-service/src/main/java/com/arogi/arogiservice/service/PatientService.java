package com.arogi.arogiservice.service;

import com.arogi.arogiservice.dto.PatientRequestDTO;
import com.arogi.arogiservice.dto.PatientResponseDTO;
import com.arogi.arogiservice.exception.EmailAlreadyExistsException;
import com.arogi.arogiservice.exception.PatientNotFoundException;
import com.arogi.arogiservice.mapper.PatientMapper;
import com.arogi.arogiservice.models.Patient;
import com.arogi.arogiservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        /*
         List<PatientResponseDTO> patientResponseDTOS = patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();
         List<PatientResponseDTO> patientResponseDTOS = patients.stream().map(PatientMapper::toDTO).toList();
         return patientResponseDTOS;
         */
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists" + patientRequestDTO.getEmail());
        }
        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: {}"+ id));

        /*
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            UUID patientId = patient.getId();
            if(patientId != id)
                throw new EmailAlreadyExistsException("A patient with this email already exists" + patientRequestDTO.getEmail());
        }
        */
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists" + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
