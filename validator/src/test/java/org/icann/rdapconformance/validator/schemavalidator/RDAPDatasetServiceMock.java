package org.icann.rdapconformance.validator.schemavalidator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.icann.rdapconformance.validator.workflow.FileSystem;
import org.icann.rdapconformance.validator.workflow.rdap.RDAPDatasetServiceImpl;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.EPPRoid;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.EventActionJsonValues;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.Ipv4AddressSpace;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.Ipv6AddressSpace;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.LinkRelations;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.MediaTypes;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.NoticeAndRemarkJsonValues;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.RDAPExtensions;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.RegistrarId;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.RegistrarIdTest;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.RoleJsonValues;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.SpecialIPv4Addresses;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.SpecialIPv6Addresses;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.StatusJsonValues;
import org.icann.rdapconformance.validator.workflow.rdap.dataset.model.VariantRelationJsonValues;

public class RDAPDatasetServiceMock extends RDAPDatasetServiceImpl {

  public RDAPDatasetServiceMock() {
    super(mock(FileSystem.class));
    this.datasetValidatorModels = List.of(
        mock(Ipv4AddressSpace.class),
        mock(SpecialIPv4Addresses.class),
        mock(Ipv6AddressSpace.class),
        mock(SpecialIPv6Addresses.class),
        mock(RDAPExtensions.class),
        mock(LinkRelations.class),
        mock(MediaTypes.class),
        mock(NoticeAndRemarkJsonValues.class),
        mock(EventActionJsonValues.class),
        mock(StatusJsonValues.class),
        mock(VariantRelationJsonValues.class),
        mock(RoleJsonValues.class),
        mock(EPPRoid.class)
    ).stream()
        .peek(mock -> doReturn(false).when(mock).isInvalid(any()))
        .collect(Collectors.toMap(Object::getClass, Function.identity()));

    RegistrarId registrarId = mock(RegistrarId.class);
    doReturn(true).when(registrarId).containsId(anyInt());
    doReturn(RegistrarIdTest.getValidRecord()).when(registrarId).getById(anyInt());
    this.datasetValidatorModels.put(registrarId.getClass(), registrarId);
  }

  @Override
  public boolean download(boolean useLocalDatasets) {
    return true;
  }

  /**
   * Special handling for mock (mocked classes are in fact artificial subclasses and do not work
   * with basic equality)
   */
  @Override
  public <T> T get(Class<T> clazz) {
    return (T) datasetValidatorModels.get(mock(clazz).getClass());
  }
}
