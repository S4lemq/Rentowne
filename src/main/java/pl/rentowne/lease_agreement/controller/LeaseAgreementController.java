package pl.rentowne.lease_agreement.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import pl.rentowne.common.controler.AbstractController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Umowy")
public class LeaseAgreementController extends AbstractController {

}
